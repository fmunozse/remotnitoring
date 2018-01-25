import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { RequestRemoteCommand } from './request-remote-command.model';
import { RequestRemoteCommandService } from './request-remote-command.service';

@Component({
    selector: 'jhi-request-remote-command-detail',
    templateUrl: './request-remote-command-detail.component.html'
})
export class RequestRemoteCommandDetailComponent implements OnInit, OnDestroy {

    requestRemoteCommand: RequestRemoteCommand;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private requestRemoteCommandService: RequestRemoteCommandService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInRequestRemoteCommands();
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
}
