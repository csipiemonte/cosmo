import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/users.service';
import { ICosmoTableColumn } from 'projects/ngx-cosmo-table/src/public-api';
import { IUser } from '../../models/user.model';
import { CosmoTableFormatter } from 'projects/ngx-cosmo-table/src/lib/model';

@Component({
  selector: 'app-cell-formatting',
  templateUrl: './cell-formatting.component.html',
  styleUrls: ['./cell-formatting.component.scss']
})
export class CellFormattingComponent implements OnInit {

  columns: ICosmoTableColumn[] = [
    { name: 'id', label: 'ID', field: '_id', canHide: false,
      headerDisplayClass: 'text-info', cellDisplayClass: 'text-info' },
    { name: 'firstName', label: 'First Name', field: 'name.first',
      formatters: CosmoTableFormatter.UPPERCASE },
    { name: 'lastName', label: 'Last Name', field: 'name.last',
      formatters: {
        format: value => '"' + value + '"'
      }
    },
    { name: 'email', label: 'Email', field: 'email',
      formatters: [
        { format: value => '*********@' + value.split('@')[1] },
        CosmoTableFormatter.UPPERCASE,
        { format: value => '(obfuscated) ' + value }
      ]
    },
    { name: 'fetchedAt', label: 'Last updated', field: 'fetchedAt',
      formatters: {formatter: CosmoTableFormatter.DATE, arguments: 'short'} },
  ];

  users: IUser[];

  constructor(private userService: UserService) {

    this.users = this.userService.getUsers(10);
  }

  ngOnInit(): void {
  }

}
