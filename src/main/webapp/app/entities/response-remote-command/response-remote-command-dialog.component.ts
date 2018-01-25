import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { ResponseRemoteCommand } from './response-remote-command.model';
import { ResponseRemoteCommandPopupService } from './response-remote-command-popup.service';
import { ResponseRemoteCommandService } from './response-remote-command.service';
import { RequestRemoteCommand, RequestRemoteCommandService } from '../request-remote-command';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-response-remote-command-dialog',
    templateUrl: './response-remote-command-dialog.component.html'
})
export class ResponseRemoteCommandDialogComponent implements OnInit {

    responseRemoteCommand: ResponseRemoteCommand;
    isSaving: boolean;

    requestremotecommands: RequestRemoteCommand[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private responseRemoteCommandService: ResponseRemoteCommandService,
        private requestRemoteCommandService: RequestRemoteCommandService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.requestRemoteCommandService.query()
            .subscribe((res: ResponseWrapper) => { this.requestremotecommands = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
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
        if (this.responseRemoteCommand.id !== undefined) {
            this.subscribeToSaveResponse(
                this.responseRemoteCommandService.update(this.responseRemoteCommand));
        } else {
            this.subscribeToSaveResponse(
                this.responseRemoteCommandService.create(this.responseRemoteCommand));
        }
    }

    private subscribeToSaveResponse(result: Observable<ResponseRemoteCommand>) {
        result.subscribe((res: ResponseRemoteCommand) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: ResponseRemoteCommand) {
        this.eventManager.broadcast({ name: 'responseRemoteCommandListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackRequestRemoteCommandById(index: number, item: RequestRemoteCommand) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-response-remote-command-popup',
    template: ''
})
export class ResponseRemoteCommandPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private responseRemoteCommandPopupService: ResponseRemoteCommandPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.responseRemoteCommandPopupService
                    .open(ResponseRemoteCommandDialogComponent as Component, params['id']);
            } else {
                this.responseRemoteCommandPopupService
                    .open(ResponseRemoteCommandDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
