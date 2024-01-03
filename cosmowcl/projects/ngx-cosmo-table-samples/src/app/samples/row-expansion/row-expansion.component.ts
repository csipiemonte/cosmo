import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/users.service';
import { ICosmoTableColumn, ICosmoTableStatusSnapshot } from 'projects/ngx-cosmo-table/src/public-api';
import { IUser } from '../../models/user.model';

@Component({
  selector: 'app-row-expansion',
  templateUrl: './row-expansion.component.html',
  styleUrls: ['./row-expansion.component.scss']
})
export class RowExpansionComponent implements OnInit {

  columns: ICosmoTableColumn[] = [
    { name: 'firstName', label: 'First Name', field: 'name.first', canHide: false },
    { name: 'lastName', label: 'Last Name', field: 'name.last' },
    { name: 'email', label: 'Email', field: 'email' },
    { name: 'status', label: 'Status',
      valueExtractor: (row: IUser) => row.isActive ? 'ACTIVE' : 'not active' },
  ];

  users: IUser[];

  constructor(private userService: UserService) {

    this.users = this.userService.getUsers(10);
  }

  ngOnInit(): void {
  }

  expandableStatusProvider = (row: IUser, status: ICosmoTableStatusSnapshot | null) => {
    return row.isActive;
  }
}
