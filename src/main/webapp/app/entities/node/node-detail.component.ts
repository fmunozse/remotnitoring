import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';
import * as crypto from 'crypto-js';

import { Node } from './node.model';
import { NodeService } from './node.service';

@Component({
    selector: 'jhi-node-detail',
    templateUrl: './node-detail.component.html'
})
export class NodeDetailComponent implements OnInit, OnDestroy {

    node: Node;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    public isCollapsed = true;
    public secretDecrypt = '';

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private nodeService: NodeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInNodes();
    }

    onKeyPassword(event: any) {
        const bytes  = crypto.AES.decrypt(this.node.secret, event.target.value);
        try {
            this.secretDecrypt = bytes.toString(crypto.enc.Utf8);
        } catch (e) {
            this.secretDecrypt = '';
        }
    }

    load(id) {
        this.nodeService.find(id).subscribe((node) => {
            this.node = node;
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

    registerChangeInNodes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'nodeListModification',
            (response) => this.load(this.node.id)
        );
    }
}
