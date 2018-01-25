import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { RequestRemoteCommand } from './request-remote-command.model';
import { RequestRemoteCommandPopupService } from './request-remote-command-popup.service';
import { RequestRemoteCommandService } from './request-remote-command.service';

@Component({
    selector: 'jhi-request-remote-command-delete-dialog',
    templateUrl: './request-remote-command-delete-dialog.component.html'
})
export class RequestRemoteCommandDeleteDialogComponent {

    requestRemoteCommand: RequestRemoteCommand;

    constructor(
        private requestRemoteCommandService: RequestRemoteCommandService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.requestRemoteCommandService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'requestRemoteCommandListModification',
                content: 'Deleted an requestRemoteCommand'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-request-remote-command-delete-popup',
    template: ''
})
export class RequestRemoteCommandDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private requestRemoteCommandPopupService: RequestRemoteCommandPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.requestRemoteCommandPopupService
                .open(RequestRemoteCommandDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
