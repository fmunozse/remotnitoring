import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { RequestRemoteCommand } from './request-remote-command.model';
import { RequestRemoteCommandPopupService } from './request-remote-command-popup.service';
import { RequestRemoteCommandService } from './request-remote-command.service';
import { Node, NodeService } from '../node';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-request-remote-command-dialog',
    templateUrl: './request-remote-command-dialog.component.html'
})
export class RequestRemoteCommandDialogComponent implements OnInit {

    requestRemoteCommand: RequestRemoteCommand;
    isSaving: boolean;

    nodes: Node[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private requestRemoteCommandService: RequestRemoteCommandService,
        private nodeService: NodeService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.nodeService.query()
            .subscribe((res: ResponseWrapper) => { this.nodes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.requestRemoteCommand.id !== undefined) {
            this.subscribeToSaveResponse(
                this.requestRemoteCommandService.update(this.requestRemoteCommand));
        } else {
            this.subscribeToSaveResponse(
                this.requestRemoteCommandService.create(this.requestRemoteCommand));
        }
    }

    private subscribeToSaveResponse(result: Observable<RequestRemoteCommand>) {
        result.subscribe((res: RequestRemoteCommand) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: RequestRemoteCommand) {
        this.eventManager.broadcast({ name: 'requestRemoteCommandListModification', content: 'OK'});
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
    selector: 'jhi-request-remote-command-popup',
    template: ''
})
export class RequestRemoteCommandPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private requestRemoteCommandPopupService: RequestRemoteCommandPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.requestRemoteCommandPopupService
                    .open(RequestRemoteCommandDialogComponent as Component, params['id']);
            } else {
                this.requestRemoteCommandPopupService
                    .open(RequestRemoteCommandDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
