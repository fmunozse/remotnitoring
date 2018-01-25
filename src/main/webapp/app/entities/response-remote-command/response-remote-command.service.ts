import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { ResponseRemoteCommand } from './response-remote-command.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ResponseRemoteCommandService {

    private resourceUrl = SERVER_API_URL + 'api/response-remote-commands';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(responseRemoteCommand: ResponseRemoteCommand): Observable<ResponseRemoteCommand> {
        const copy = this.convert(responseRemoteCommand);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(responseRemoteCommand: ResponseRemoteCommand): Observable<ResponseRemoteCommand> {
        const copy = this.convert(responseRemoteCommand);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<ResponseRemoteCommand> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to ResponseRemoteCommand.
     */
    private convertItemFromServer(json: any): ResponseRemoteCommand {
        const entity: ResponseRemoteCommand = Object.assign(new ResponseRemoteCommand(), json);
        entity.whenExecuted = this.dateUtils
            .convertDateTimeFromServer(json.whenExecuted);
        return entity;
    }

    /**
     * Convert a ResponseRemoteCommand to a JSON which can be sent to the server.
     */
    private convert(responseRemoteCommand: ResponseRemoteCommand): ResponseRemoteCommand {
        const copy: ResponseRemoteCommand = Object.assign({}, responseRemoteCommand);

        copy.whenExecuted = this.dateUtils.toDate(responseRemoteCommand.whenExecuted);
        return copy;
    }
}
