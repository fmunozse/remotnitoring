import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { ResponseRemoteCommand } from './response-remote-command.model';
import { ResponseRemoteCommandService } from './response-remote-command.service';

@Component({
    selector: 'jhi-response-remote-command-detail',
    templateUrl: './response-remote-command-detail.component.html'
})
export class ResponseRemoteCommandDetailComponent implements OnInit, OnDestroy {

    responseRemoteCommand: ResponseRemoteCommand;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private responseRemoteCommandService: ResponseRemoteCommandService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInResponseRemoteCommands();
    }

    load(id) {
        this.responseRemoteCommandService.find(id).subscribe((responseRemoteCommand) => {
            this.responseRemoteCommand = responseRemoteCommand;
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

    registerChangeInResponseRemoteCommands() {
        this.eventSubscriber = this.eventManager.subscribe(
            'responseRemoteCommandListModification',
            (response) => this.load(this.responseRemoteCommand.id)
        );
    }
}
