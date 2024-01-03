import { Component, OnInit, ViewChild } from '@angular/core';
import { UserService } from '../../services/users.service';
import { CosmoTableComponent, ICosmoTableColumn, ICosmoTableContextFilter, ICosmoTablePageRequest, ICosmoTableReloadContext } from 'projects/ngx-cosmo-table/src/public-api';
import { IUser } from '../../models/user.model';
import { of } from 'rxjs';

@Component({
  selector: 'app-checkbox-filtering',
  templateUrl: './checkbox-filtering.component.html',
  styleUrls: ['./checkbox-filtering.component.scss']
})
export class CheckboxFilteringComponent implements OnInit {

  columns: ICosmoTableColumn[] = [
    { name: 'id', label: 'ID', field: '_id', canHide: false },
    { name: 'firstName', label: 'First Name', field: 'name.first',
      canFilter: true, defaultFilter: true },
    { name: 'lastName', label: 'Last Name', field: 'name.last',
      canFilter: true, defaultFilter: true },
    { name: 'email', label: 'Email', field: 'email', canFilter: true },
    { name: 'guid', label: 'GUID', field: 'guid' },
  ];

  @ViewChild('table') table: CosmoTableComponent | null = null;

  filters: ICosmoTableContextFilter[] = [
    { name: 'shortName', label: 'Short first name', group: 'name' },
    { name: 'longName', label: 'Long first name', group: 'name' },
    { name: 'shortLastName', label: 'Short last name', group: 'lname' },
    { name: 'longLastName', label: 'Long last name', group: 'lname' }
  ];

  constructor(private userService: UserService) {
  }

  ngOnInit(): void {
  }

  dataProvider = (input: ICosmoTablePageRequest, context?: ICosmoTableReloadContext) => {
    let output = this.userService.getUsers(10);
    const limit = 7;

    if (input.filters?.indexOf('shortName') !== -1) {
      output = output.filter((row: IUser) => row.name.first.length < limit);
    }
    if (input.filters?.indexOf('longName') !== -1) {
      output = output.filter((row: IUser) => row.name.first.length >= limit);
    }
    if (input.filters?.indexOf('shortLastName') !== -1) {
      output = output.filter((row: IUser) => row.name.last.length < limit);
    }
    if (input.filters?.indexOf('longLastName') !== -1) {
      output = output.filter((row: IUser) => row.name.last.length >= limit);
    }

    return of({
      content: output
    });
  }

}
