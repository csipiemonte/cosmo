import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/users.service';
import { ICosmoTableColumn } from 'projects/ngx-cosmo-table/src/public-api';
import { IUser } from '../../models/user.model';

@Component({
  selector: 'app-selection',
  templateUrl: './selection.component.html',
  styleUrls: ['./selection.component.scss']
})
export class SelectionComponent implements OnInit {

  columns: ICosmoTableColumn[] = [
    { name: 'id', label: 'ID', field: '_id', canHide: false },
    { name: 'firstName', label: 'First Name', field: 'name.first' },
    { name: 'lastName', label: 'Last Name', field: 'name.last' },
    { name: 'email', label: 'Email', field: 'email' },
    { name: 'guid', label: 'GUID', field: 'guid' },
  ];

  users: IUser[];

  selectedUsers: IUser[] = [];

  constructor(private userService: UserService) {

    this.users = this.userService.getUsers(10);
  }

  ngOnInit(): void {
  }

  selectionChangeHandler(selectedUsers: IUser[]) {
    this.selectedUsers = selectedUsers;
  }

}
