import { Component, OnInit, ViewChild } from '@angular/core';
import { UserService } from '../../services/users.service';
import { CosmoTableComponent, ICosmoTableColumn } from 'projects/ngx-cosmo-table/src/public-api';
import { IUser } from '../../models/user.model';

@Component({
  selector: 'app-filtering',
  templateUrl: './filtering.component.html',
  styleUrls: ['./filtering.component.scss']
})
export class FilteringComponent implements OnInit {

  columns: ICosmoTableColumn[] = [
    { name: 'id', label: 'ID', field: '_id', canHide: false },
    { name: 'firstName', label: 'First Name', field: 'name.first',
      canFilter: true, defaultFilter: true },
    { name: 'lastName', label: 'Last Name', field: 'name.last',
      canFilter: true, defaultFilter: true },
    { name: 'email', label: 'Email', field: 'email', canFilter: true },
    { name: 'guid', label: 'GUID', field: 'guid' },
  ];

  users: IUser[];

  @ViewChild('table') table: CosmoTableComponent | null = null;

  constructor(private userService: UserService) {

    this.users = this.userService.getUsers(10);
  }

  ngOnInit(): void {
  }

}
