import { Component, OnInit, ViewChild } from '@angular/core';
import { UserService } from '../../services/users.service';
import { ICosmoTableColumn, ICosmoTableItemDroppedContext } from 'projects/ngx-cosmo-table/src/public-api';
import { IUser } from '../../models/user.model';
import { CosmoTableComponent } from 'projects/ngx-cosmo-table/src/public-api';

@Component({
  selector: 'app-drag-and-drop',
  templateUrl: './drag-and-drop.component.html',
  styleUrls: ['./drag-and-drop.component.scss']
})
export class DragAndDropComponent implements OnInit {

  columns: ICosmoTableColumn[] = [
    { name: 'id', label: 'ID', field: '_id', canHide: false },
    { name: 'firstName', label: 'First Name', field: 'name.first' },
    { name: 'lastName', label: 'Last Name', field: 'name.last' },
    { name: 'email', label: 'Email', field: 'email' },
    { name: 'guid', label: 'GUID', field: 'guid' },
  ];

  users1: IUser[];
  users2: IUser[];

  @ViewChild('table1') table1: CosmoTableComponent | null = null;
  @ViewChild('table2') table2: CosmoTableComponent | null = null;

  constructor(private userService: UserService) {

    this.users1 = this.userService.getUsers(10);
    this.users2 = [];
  }

  ngOnInit(): void {
  }

  onItemDropped(eventPayload: ICosmoTableItemDroppedContext) {

    this.users1.splice(this.users1.indexOf(eventPayload.item), 1);
    this.users2.splice(eventPayload.event.currentIndex, 0, eventPayload.item);

    setTimeout(() => this.table1?.refresh(), 500);
    setTimeout(() => this.table2?.refresh(), 500);
  }

}
