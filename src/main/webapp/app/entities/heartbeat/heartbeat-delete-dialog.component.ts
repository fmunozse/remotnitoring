import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Heartbeat } from './heartbeat.model';
import { HeartbeatPopupService } from './heartbeat-popup.service';
import { HeartbeatService } from './heartbeat.service';

@Component({
    selector: 'jhi-heartbeat-delete-dialog',
    templateUrl: './heartbeat-delete-dialog.component.html'
})
export class HeartbeatDeleteDialogComponent {

    heartbeat: Heartbeat;

    constructor(
        private heartbeatService: HeartbeatService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.heartbeatService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'heartbeatListModification',
                content: 'Deleted an heartbeat'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-heartbeat-delete-popup',
    template: ''
})
export class HeartbeatDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private heartbeatPopupService: HeartbeatPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.heartbeatPopupService
                .open(HeartbeatDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
