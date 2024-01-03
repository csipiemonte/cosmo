import { Component, OnInit, ViewChild } from '@angular/core';
import { UserService } from '../../services/users.service';
import { ICosmoTableColumn, ICosmoTableItemDroppedContext } from 'projects/ngx-cosmo-table/src/public-api';
import { IUser } from '../../models/user.model';
import { moveItemInArray } from '@angular/cdk/drag-drop';
import { CosmoTableComponent } from 'projects/ngx-cosmo-table/src/public-api';

@Component({
  selector: 'app-reorder',
  templateUrl: './reorder.component.html',
  styleUrls: ['./reorder.component.scss']
})
export class ReorderComponent implements OnInit {

  columns: ICosmoTableColumn[] = [
    { name: 'id', label: 'ID', field: '_id', canHide: false },
    { name: 'status', label: 'status', showLabelInTable: false, applyTemplate: true },
    { name: 'firstName', label: 'First Name', field: 'name.first' },
    { name: 'lastName', label: 'Last Name', field: 'name.last' },
    { name: 'email', label: 'Email', field: 'email' },
    { name: 'guid', label: 'GUID', field: 'guid' },
  ];

  users: IUser[];

  @ViewChild(CosmoTableComponent) table: CosmoTableComponent | null = null;

  constructor(private userService: UserService) {

    this.users = this.userService.getUsers(10);
  }

  ngOnInit(): void {
  }

  onItemDropped(eventPayload: ICosmoTableItemDroppedContext) {
    const selected: IUser = this.users[eventPayload.event.previousIndex];
    (selected as any).moved = true;

    moveItemInArray(
      this.users,
      eventPayload.event.previousIndex,
      eventPayload.event.currentIndex
     );

    setTimeout(() => this.table?.refresh(), 500);
  }

}
