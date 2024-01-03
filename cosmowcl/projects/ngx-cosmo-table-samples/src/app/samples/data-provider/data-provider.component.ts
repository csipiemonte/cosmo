import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/users.service';
import { ICosmoTableColumn,
  ICosmoTablePageRequest,
  ICosmoTableReloadContext,
  ICosmoTablePageResponse } from 'projects/ngx-cosmo-table/src/public-api';

@Component({
  selector: 'app-data-provider',
  templateUrl: './data-provider.component.html',
  styleUrls: ['./data-provider.component.scss']
})
export class DataProviderComponent implements OnInit {

  columns: ICosmoTableColumn[] = [
    { name: 'id', label: 'ID', field: '_id', canHide: false },
    { name: 'firstName', label: 'First Name', field: 'name.first' },
    { name: 'lastName', label: 'Last Name', field: 'name.last' },
    { name: 'email', label: 'Email', field: 'email' },
    { name: 'guid', label: 'GUID', field: 'guid' },
  ];

  constructor(private userService: UserService) {
  }

  passedToBackend: any = null;

  ngOnInit(): void {
  }

  dataProvider = (input: ICosmoTablePageRequest, context?: ICosmoTableReloadContext) => {
    this.passedToBackend = input;
    return this.userService.fetchUsersFromBackend(input);
  }

}
