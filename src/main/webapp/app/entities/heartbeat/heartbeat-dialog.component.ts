import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Heartbeat } from './heartbeat.model';
import { HeartbeatPopupService } from './heartbeat-popup.service';
import { HeartbeatService } from './heartbeat.service';
import { Node, NodeService } from '../node';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-heartbeat-dialog',
    templateUrl: './heartbeat-dialog.component.html'
})
export class HeartbeatDialogComponent implements OnInit {

    heartbeat: Heartbeat;
    isSaving: boolean;

    nodes: Node[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private heartbeatService: HeartbeatService,
        private nodeService: NodeService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.nodeService.query()
            .subscribe((res: ResponseWrapper) => { this.nodes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.heartbeat.id !== undefined) {
            this.subscribeToSaveResponse(
                this.heartbeatService.update(this.heartbeat));
        } else {
            this.subscribeToSaveResponse(
                this.heartbeatService.create(this.heartbeat));
        }
    }

    private subscribeToSaveResponse(result: Observable<Heartbeat>) {
        result.subscribe((res: Heartbeat) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Heartbeat) {
        this.eventManager.broadcast({ name: 'heartbeatListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackNodeById(index: number, item: Node) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-heartbeat-popup',
    template: ''
})
export class HeartbeatPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private heartbeatPopupService: HeartbeatPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.heartbeatPopupService
                    .open(HeartbeatDialogComponent as Component, params['id']);
            } else {
                this.heartbeatPopupService
                    .open(HeartbeatDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
