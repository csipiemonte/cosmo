import { Component, OnInit, ViewChild } from '@angular/core';
import { UserService } from '../../services/users.service';
import {
  ICosmoTableColumn,
  ICosmoTableAction,
  CosmoTableActionActivationCondition,
  ICosmoTableActionDispatchingContext,
  ICosmoTableStatusSnapshot,
  CosmoTableComponent} from 'projects/ngx-cosmo-table/src/public-api';
import { IUser } from '../../models/user.model';

@Component({
  selector: 'app-context-actions',
  templateUrl: './context-actions.component.html',
  styleUrls: ['./context-actions.component.scss']
})
export class ContextActionsComponent implements OnInit {

  columns: ICosmoTableColumn[] = [
    { name: 'id', label: 'ID', field: '_id', canHide: false },
    { name: 'firstName', label: 'First Name', field: 'name.first' },
    { name: 'lastName', label: 'Last Name', field: 'name.last' },
    { name: 'email', label: 'Email', field: 'email' },
    { name: 'status', label: 'Status',
      valueExtractor: (row: IUser) => row.isActive ? 'ACTIVE' : 'not active' },
  ];

  users: IUser[];

  actions: ICosmoTableAction[] = [
    { name: 'alert', label: 'Alert',
      icon: 'fas fa-bell',
      activationCondition: CosmoTableActionActivationCondition.MULTIPLE_SELECTION },
    { name: 'clone', label: 'Clone',
      activationCondition: CosmoTableActionActivationCondition.SINGLE_SELECTION },
    { name: 'delete', label: 'Delete',
      activationCondition: CosmoTableActionActivationCondition.DYNAMIC }
  ];

  @ViewChild('table') table: CosmoTableComponent | null = null;

  constructor(private userService: UserService) {

    this.users = this.userService.getUsers(10);
  }

  ngOnInit(): void {
    // NOP
  }

  onAction(context: ICosmoTableActionDispatchingContext) {

    if (context.action.name === 'alert' ) {
      alert('You selected: ' +
        context.selectedItems
          .map(i => i.name.first + ' ' + i.name.last)
          .join(', ')
        );
    } else {
      alert('You clicked on action ' + context.action.name);
    }
  }

  actionStatusProvider = (action: ICosmoTableAction, status: ICosmoTableStatusSnapshot | null) => {
    if (action.name === 'delete') {
      return (status?.checkedItems || []).length > 0 &&
        (status?.checkedItems || []).filter((item: IUser) => item.isActive).length === 0;
    } else {
      return false;
    }
  }
}
