import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiDataUtils, JhiAlertService } from 'ng-jhipster';

import { RequestRemoteCommand } from './request-remote-command.model';
import { RequestRemoteCommandService } from './request-remote-command.service';

import { ResponseRemoteCommand } from '../response-remote-command/response-remote-command.model';
import { ResponseRemoteCommandService } from '../response-remote-command/response-remote-command.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-request-remote-command-detail',
    templateUrl: './request-remote-command-detail.component.html'
})
export class RequestRemoteCommandDetailComponent implements OnInit, OnDestroy {

    requestRemoteCommand: RequestRemoteCommand;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    responseRemoteCommands: ResponseRemoteCommand[];

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private requestRemoteCommandService: RequestRemoteCommandService,
        private responseRemoteCommandService: ResponseRemoteCommandService,
        private jhiAlertService: JhiAlertService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
            this.loadResponseRemoteCommands(params['id']);
        });
        this.registerChangeInRequestRemoteCommands();
    }

    loadResponseRemoteCommands(id) {
        this.responseRemoteCommandService.getResponseRemoteCommandsByRequest(id, {
            page: 0,
            size: 100,
            sort: ['whenExecuted,desc']}).subscribe(
            (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    load(id) {
        this.requestRemoteCommandService.find(id).subscribe((requestRemoteCommand) => {
            this.requestRemoteCommand = requestRemoteCommand;
        });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInRequestRemoteCommands() {
        this.eventSubscriber = this.eventManager.subscribe(
            'requestRemoteCommandListModification',
            (response) => this.load(this.requestRemoteCommand.id)
        );
    }

    private onSuccess(data, headers) {
        this.responseRemoteCommands = data;
    }
    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
