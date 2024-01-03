import { Component, OnInit, ViewChild } from '@angular/core';
import { UserService } from '../../services/users.service';
import { CosmoTableComponent, ICosmoTableColumn } from 'projects/ngx-cosmo-table/src/public-api';
import { IUser } from '../../models/user.model';

@Component({
  selector: 'app-pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.scss']
})
export class PaginationComponent implements OnInit {

  columns: ICosmoTableColumn[] = [
    { name: 'id', label: 'ID', field: '_id', canHide: false },
    { name: 'firstName', label: 'First Name', field: 'name.first', canSort: true },
    { name: 'lastName', label: 'Last Name', field: 'name.last', canSort: true },
    { name: 'email', label: 'Email', field: 'email', canSort: true },
    { name: 'guid', label: 'GUID', field: 'guid' },
  ];

  users: IUser[];

  constructor(private userService: UserService) {

    this.users = this.userService.getUsers(100);
  }

  @ViewChild('table') table: CosmoTableComponent | null = null;

  ngOnInit(): void {
  }

}
