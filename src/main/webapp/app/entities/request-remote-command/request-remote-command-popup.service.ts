import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { RequestRemoteCommand } from './request-remote-command.model';
import { RequestRemoteCommandService } from './request-remote-command.service';

@Injectable()
export class RequestRemoteCommandPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private requestRemoteCommandService: RequestRemoteCommandService

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
                this.requestRemoteCommandService.find(id).subscribe((requestRemoteCommand) => {
                    this.ngbModalRef = this.requestRemoteCommandModalRef(component, requestRemoteCommand);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.requestRemoteCommandModalRef(component, new RequestRemoteCommand());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    requestRemoteCommandModalRef(component: Component, requestRemoteCommand: RequestRemoteCommand): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.requestRemoteCommand = requestRemoteCommand;
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
