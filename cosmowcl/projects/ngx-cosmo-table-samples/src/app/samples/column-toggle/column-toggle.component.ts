import { Component, OnInit, ViewChild } from '@angular/core';
import { UserService } from '../../services/users.service';
import { CosmoTableComponent, ICosmoTableColumn } from 'projects/ngx-cosmo-table/src/public-api';
import { IUser } from '../../models/user.model';

@Component({
  selector: 'app-column-toggle',
  templateUrl: './column-toggle.component.html',
  styleUrls: ['./column-toggle.component.scss']
})
export class ColumnToggleComponent implements OnInit {

  columns: ICosmoTableColumn[] = [
    { name: 'id', label: 'ID', field: '_id', canHide: false },
    { name: 'firstName', label: 'First Name', field: 'name.first' },
    { name: 'lastName', label: 'Last Name', field: 'name.last' },
    { name: 'email', label: 'Email', field: 'email' },
    { name: 'guid', label: 'GUID', field: 'guid', defaultVisible: false },
  ];

  users: IUser[];

  @ViewChild('table') table: CosmoTableComponent | null = null;

  constructor(private userService: UserService) {

    this.users = this.userService.getUsers(10);
  }

  ngOnInit(): void {
  }

}
