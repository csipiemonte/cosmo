import { Component, OnInit, ViewChild } from '@angular/core';
import { UserService } from '../../services/users.service';
import { ICosmoTableColumn, CosmoTableFormatter,
  CosmoTableComponent } from 'projects/ngx-cosmo-table/src/public-api';
import { IUser } from '../../models/user.model';
import { of, Observable, interval } from 'rxjs';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-observable-data',
  templateUrl: './observable-data.component.html',
  styleUrls: ['./observable-data.component.scss']
})
export class ObservableDataComponent implements OnInit {

  columns: ICosmoTableColumn[] = [
    { name: 'id', label: 'ID', field: '_id', canHide: false },
    { name: 'firstName', label: 'First Name', field: 'name.first' },
    { name: 'lastName', label: 'Last Name', field: 'name.last' },
    { name: 'email', label: 'Email', field: 'email' },
    { name: 'fetchedAt', label: 'Last updated', field: 'fetchedAt',
      formatters: {formatter: CosmoTableFormatter.DATE, arguments: 'hh:MM:ss'} },
  ];

  dataObservable: Observable<IUser[]> | null = null;

  constructor(private userService: UserService) {
  }

  ngOnInit(): void {

    this.dataObservable = interval(3000).pipe(
      map( (index: number) => {
        return this.userService.getUsers(5);
      } )
    ); // emits every 5 seconds
  }

}
