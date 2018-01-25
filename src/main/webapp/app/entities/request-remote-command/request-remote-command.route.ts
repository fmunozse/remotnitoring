import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { RequestRemoteCommandComponent } from './request-remote-command.component';
import { RequestRemoteCommandDetailComponent } from './request-remote-command-detail.component';
import { RequestRemoteCommandPopupComponent } from './request-remote-command-dialog.component';
import { RequestRemoteCommandDeletePopupComponent } from './request-remote-command-delete-dialog.component';

@Injectable()
export class RequestRemoteCommandResolvePagingParams implements Resolve<any> {

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

export const requestRemoteCommandRoute: Routes = [
    {
        path: 'request-remote-command',
        component: RequestRemoteCommandComponent,
        resolve: {
            'pagingParams': RequestRemoteCommandResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'RequestRemoteCommands'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'request-remote-command/:id',
        component: RequestRemoteCommandDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'RequestRemoteCommands'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const requestRemoteCommandPopupRoute: Routes = [
    {
        path: 'request-remote-command-new',
        component: RequestRemoteCommandPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'RequestRemoteCommands'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'request-remote-command/:id/edit',
        component: RequestRemoteCommandPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'RequestRemoteCommands'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'request-remote-command/:id/delete',
        component: RequestRemoteCommandDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'RequestRemoteCommands'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
