import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ResponseRemoteCommandComponent } from './response-remote-command.component';
import { ResponseRemoteCommandDetailComponent } from './response-remote-command-detail.component';
import { ResponseRemoteCommandPopupComponent } from './response-remote-command-dialog.component';
import { ResponseRemoteCommandDeletePopupComponent } from './response-remote-command-delete-dialog.component';

@Injectable()
export class ResponseRemoteCommandResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const responseRemoteCommandRoute: Routes = [
    {
        path: 'response-remote-command',
        component: ResponseRemoteCommandComponent,
        resolve: {
            'pagingParams': ResponseRemoteCommandResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ResponseRemoteCommands'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'response-remote-command/:id',
        component: ResponseRemoteCommandDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ResponseRemoteCommands'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const responseRemoteCommandPopupRoute: Routes = [
    {
        path: 'response-remote-command-new',
        component: ResponseRemoteCommandPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ResponseRemoteCommands'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'response-remote-command/:id/edit',
        component: ResponseRemoteCommandPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ResponseRemoteCommands'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'response-remote-command/:id/delete',
        component: ResponseRemoteCommandDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ResponseRemoteCommands'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
