import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';
import * as crypto from 'crypto-js';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { Node } from './node.model';
import { NodePopupService } from './node-popup.service';
import { NodeService } from './node.service';
import { User, UserService } from '../../shared';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-node-dialog',
    templateUrl: './node-dialog.component.html'
})
export class NodeDialogComponent implements OnInit {

    node: Node;
    isSaving: boolean;
    isAllowEditSecret = false;

    users: User[];
    renewDayDp: any;
    secretDecrypt = '';
    secretOriginalValue: any;
    secretChanged = false;

    fieldPassword = '';
    fieldPasswordConfirm = '';

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private nodeService: NodeService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; } , (res: ResponseWrapper) => this.onError(res.json));
        this.secretOriginalValue = this.node.secret ;
    }

    updateSecrets() {
        if (this.fieldPasswordConfirm !== '' && this.fieldPasswordConfirm === this.fieldPassword) {
            this.isAllowEditSecret = true;
            if (this.secretDecrypt !== '') {
                const ciphertext = crypto.AES.encrypt(this.secretDecrypt, this.fieldPasswordConfirm);
                this.node.secret = ciphertext.toString();
            }

        } else {

            // In case pwds are different is not allow edit and it should be back to original value the secret
            this.isAllowEditSecret = false;
            this.node.secret = this.secretOriginalValue;

            if (this.fieldPassword !== '') {
                const bytes  = crypto.AES.decrypt(this.node.secret, this.fieldPassword);
                try {
                    this.secretDecrypt = bytes.toString(crypto.enc.Utf8);
                } catch (e) {
                    this.secretDecrypt = '';
                }
            }
        }
    }

    onKeyPassword(event: any) {
        // const pwd = event.target.value;
        /*
        const bytes  = crypto.AES.decrypt(this.node.secret, this.fieldPassword);
        try {
            this.secretDecrypt = bytes.toString(crypto.enc.Utf8);
        } catch (e) {
            this.secretDecrypt = '';
        }
        */
        this.updateSecrets();
    }

    onKeyPasswordConfirm(event: any) {
        // const pwdConfirm = event.target.value;
        /*
        if (this.fieldPasswordConfirm !== '' && this.fieldPasswordConfirm === this.fieldPassword) {
            this.isAllowEditSecret = true;
        } else {
            this.isAllowEditSecret = false;
            console.log (this.secretOriginalValue);
            this.node.secret = this.secretOriginalValue;
        }
        */
        this.updateSecrets();
    }

    onKeySecrect(event: any) {
        /*
        const ciphertext = crypto.AES.encrypt(this.secretDecrypt, this.fieldPasswordConfirm);
        this.node.secret = ciphertext.toString();
        */
        this.updateSecrets();
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
        if (this.node.id !== undefined) {
            this.subscribeToSaveResponse(
                this.nodeService.update(this.node));
        } else {
            this.subscribeToSaveResponse(
                this.nodeService.create(this.node));
        }
    }

    private subscribeToSaveResponse(result: Observable<Node>) {
        result.subscribe((res: Node) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Node) {
        this.eventManager.broadcast({ name: 'nodeListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-node-popup',
    template: ''
})
export class NodePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private nodePopupService: NodePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.nodePopupService
                    .open(NodeDialogComponent as Component, params['id']);
            } else {
                this.nodePopupService
                    .open(NodeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
