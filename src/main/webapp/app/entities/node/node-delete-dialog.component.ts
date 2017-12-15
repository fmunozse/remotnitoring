import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Node } from './node.model';
import { NodePopupService } from './node-popup.service';
import { NodeService } from './node.service';

@Component({
    selector: 'jhi-node-delete-dialog',
    templateUrl: './node-delete-dialog.component.html'
})
export class NodeDeleteDialogComponent {

    node: Node;

    constructor(
        private nodeService: NodeService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.nodeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'nodeListModification',
                content: 'Deleted an node'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-node-delete-popup',
    template: ''
})
export class NodeDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private nodePopupService: NodePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.nodePopupService
                .open(NodeDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
