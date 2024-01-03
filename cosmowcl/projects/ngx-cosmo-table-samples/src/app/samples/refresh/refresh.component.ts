import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { UserService } from '../../services/users.service';
import { ICosmoTableColumn, CosmoTableFormatter,
  ICosmoTablePageRequest, ICosmoTableReloadContext,
  ICosmoTablePageResponse, ICosmoTablePushRefreshRequest,
  CosmoTableComponent } from 'projects/ngx-cosmo-table/src/public-api';
import { IUser } from '../../models/user.model';
import { of, Observable, interval } from 'rxjs';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-refresh',
  templateUrl: './refresh.component.html',
  styleUrls: ['./refresh.component.scss']
})
export class RefreshComponent implements OnInit, OnDestroy {

  columns: ICosmoTableColumn[] = [
    { name: 'id', label: 'ID', field: '_id', canHide: false },
    { name: 'firstName', label: 'First Name', field: 'name.first' },
    { name: 'lastName', label: 'Last Name', field: 'name.last' },
    { name: 'email', label: 'Email', field: 'email' },
    { name: 'fetchedAt', label: 'Last updated', field: 'fetchedAt',
      formatters: {formatter: CosmoTableFormatter.DATE, arguments: 'hh:MM:ss'} },
  ];

  refreshEmitter: Observable<ICosmoTablePushRefreshRequest> | null = null;

  dataObservable: Observable<IUser[]> | null = null;

  @ViewChild('tableNotRefreshingAutomatically') tableNotRefreshingAutomatically: CosmoTableComponent | null = null;

  constructor(private userService: UserService) {
  }

  ngOnInit(): void {
    this.refreshEmitter = interval(3000).pipe(
      map( (index: number) => {
        return { event: index, inBackground: true };
      } )
    ); // emits every 3 seconds

    this.dataObservable = interval(5000).pipe(
      map( (index: number) => {
        return this.userService.getUsers(5);
      } )
    ); // emits every 5 seconds
  }

  dataProvider = (input: ICosmoTablePageRequest, context?: ICosmoTableReloadContext) => {
    const output: ICosmoTablePageResponse = {
      content: this.userService.getUsers(5)
    };
    return of(output);
  }

  refreshManually(): void {
    this.tableNotRefreshingAutomatically?.refresh();
  }

  ngOnDestroy(): void {
    this.refreshEmitter = null;
    this.dataObservable = null;
  }
}
