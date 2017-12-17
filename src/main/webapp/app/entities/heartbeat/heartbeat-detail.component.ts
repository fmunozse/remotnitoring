import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Heartbeat } from './heartbeat.model';
import { HeartbeatService } from './heartbeat.service';

@Component({
    selector: 'jhi-heartbeat-detail',
    templateUrl: './heartbeat-detail.component.html'
})
export class HeartbeatDetailComponent implements OnInit, OnDestroy {

    heartbeat: Heartbeat;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private heartbeatService: HeartbeatService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInHeartbeats();
    }

    load(id) {
        this.heartbeatService.find(id).subscribe((heartbeat) => {
            this.heartbeat = heartbeat;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInHeartbeats() {
        this.eventSubscriber = this.eventManager.subscribe(
            'heartbeatListModification',
            (response) => this.load(this.heartbeat.id)
        );
    }
}
