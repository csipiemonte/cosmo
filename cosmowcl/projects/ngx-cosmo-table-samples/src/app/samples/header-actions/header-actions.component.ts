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
  selector: 'app-header-actions',
  templateUrl: './header-actions.component.html',
  styleUrls: ['./header-actions.component.scss']
})
export class HeaderActionsComponent implements OnInit {

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
    { name: 'nothing', label: 'Nothing',
      activationCondition: CosmoTableActionActivationCondition.ALWAYS },
    { name: 'alert', label: 'Alert', displayClass: 'warning',
      icon: 'fas fa-bell',
      activationCondition: CosmoTableActionActivationCondition.MULTIPLE_SELECTION },
    { name: 'new', label: 'New', displayClass: 'outline-primary',
      activationCondition: CosmoTableActionActivationCondition.NO_SELECTION },
    { name: 'clone', label: 'Clone', displayClass: 'success',
      activationCondition: CosmoTableActionActivationCondition.SINGLE_SELECTION },
    { name: 'delete', label: 'Delete', displayClass: 'danger',
      activationCondition: CosmoTableActionActivationCondition.DYNAMIC }
  ];

  @ViewChild('table') table: CosmoTableComponent | null = null;

  constructor(private userService: UserService) {

    this.users = this.userService.getUsers(10);
  }

  ngOnInit(): void {
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
      return (status?.checkedItems?.length || 0) > 0 &&
        status?.checkedItems?.filter((item: IUser) => item.isActive).length === 0;
    } else {
      return false;
    }
  }
}
