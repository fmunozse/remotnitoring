import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { RequestRemoteCommand } from './request-remote-command.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class RequestRemoteCommandService {

    private resourceUrl = SERVER_API_URL + 'api/request-remote-commands';

    constructor(private http: Http) { }

    create(requestRemoteCommand: RequestRemoteCommand): Observable<RequestRemoteCommand> {
        const copy = this.convert(requestRemoteCommand);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(requestRemoteCommand: RequestRemoteCommand): Observable<RequestRemoteCommand> {
        const copy = this.convert(requestRemoteCommand);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<RequestRemoteCommand> {
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
     * Convert a returned JSON object to RequestRemoteCommand.
     */
    private convertItemFromServer(json: any): RequestRemoteCommand {
        const entity: RequestRemoteCommand = Object.assign(new RequestRemoteCommand(), json);
        return entity;
    }

    /**
     * Convert a RequestRemoteCommand to a JSON which can be sent to the server.
     */
    private convert(requestRemoteCommand: RequestRemoteCommand): RequestRemoteCommand {
        const copy: RequestRemoteCommand = Object.assign({}, requestRemoteCommand);
        return copy;
    }
}
