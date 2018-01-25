import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { ResponseRemoteCommand } from './response-remote-command.model';
import { ResponseRemoteCommandService } from './response-remote-command.service';

@Injectable()
export class ResponseRemoteCommandPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private responseRemoteCommandService: ResponseRemoteCommandService

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
                this.responseRemoteCommandService.find(id).subscribe((responseRemoteCommand) => {
                    responseRemoteCommand.whenExecuted = this.datePipe
                        .transform(responseRemoteCommand.whenExecuted, 'yyyy-MM-ddTHH:mm:ss');
                    this.ngbModalRef = this.responseRemoteCommandModalRef(component, responseRemoteCommand);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.responseRemoteCommandModalRef(component, new ResponseRemoteCommand());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    responseRemoteCommandModalRef(component: Component, responseRemoteCommand: ResponseRemoteCommand): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.responseRemoteCommand = responseRemoteCommand;
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
