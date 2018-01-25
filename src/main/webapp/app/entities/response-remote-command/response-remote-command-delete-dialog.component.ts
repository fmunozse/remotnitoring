import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ResponseRemoteCommand } from './response-remote-command.model';
import { ResponseRemoteCommandPopupService } from './response-remote-command-popup.service';
import { ResponseRemoteCommandService } from './response-remote-command.service';

@Component({
    selector: 'jhi-response-remote-command-delete-dialog',
    templateUrl: './response-remote-command-delete-dialog.component.html'
})
export class ResponseRemoteCommandDeleteDialogComponent {

    responseRemoteCommand: ResponseRemoteCommand;

    constructor(
        private responseRemoteCommandService: ResponseRemoteCommandService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.responseRemoteCommandService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'responseRemoteCommandListModification',
                content: 'Deleted an responseRemoteCommand'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-response-remote-command-delete-popup',
    template: ''
})
export class ResponseRemoteCommandDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private responseRemoteCommandPopupService: ResponseRemoteCommandPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.responseRemoteCommandPopupService
                .open(ResponseRemoteCommandDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
