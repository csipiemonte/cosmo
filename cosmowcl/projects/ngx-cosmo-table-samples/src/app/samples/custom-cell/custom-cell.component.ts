import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/users.service';
import { ICosmoTableColumn } from 'projects/ngx-cosmo-table/src/public-api';
import { IUser } from '../../models/user.model';

@Component({
  selector: 'app-custom-cell',
  templateUrl: './custom-cell.component.html',
  styleUrls: ['./custom-cell.component.scss']
})
export class CustomCellComponent implements OnInit {

  columns: ICosmoTableColumn[] = [
    { name: 'picture32', label: 'picture', field: 'picture', showLabelInTable: false, applyTemplate: true },
    { name: 'id', label: 'ID', field: '_id', canHide: false },
    { name: 'fullName', label: 'Full Name',
      valueExtractor: row => row.name.first + ' ' + row.name.last },
    { name: 'email', label: 'Email', field: 'email' },
    { name: 'status', label: 'Status', field: 'isActive', applyTemplate: true },
  ];

  users: IUser[];

  constructor(private userService: UserService) {

    this.users = this.userService.getUsers(10);
  }

  rowClassProvider = (row: any) => {
    return row.isActive ? 'table-success' : '';
  }

  ngOnInit(): void {
  }

}
