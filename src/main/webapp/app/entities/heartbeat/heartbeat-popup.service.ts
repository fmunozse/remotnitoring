import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { Heartbeat } from './heartbeat.model';
import { HeartbeatService } from './heartbeat.service';

@Injectable()
export class HeartbeatPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private heartbeatService: HeartbeatService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.heartbeatService.find(id).subscribe((heartbeat) => {
                    heartbeat.timestamp = this.datePipe
                        .transform(heartbeat.timestamp, 'yyyy-MM-ddTHH:mm:ss');
                    this.ngbModalRef = this.heartbeatModalRef(component, heartbeat);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.heartbeatModalRef(component, new Heartbeat());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    heartbeatModalRef(component: Component, heartbeat: Heartbeat): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.heartbeat = heartbeat;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
