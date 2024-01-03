import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/users.service';
import { ICosmoTableColumn, ICosmoTableStoreAdapter,
  ICosmoTableStoreAdapterSaveContext, ICosmoTablePersistableStatusSnapshot
} from 'projects/ngx-cosmo-table/src/public-api';
import { IUser } from '../../models/user.model';
import { Observable, of } from 'rxjs';

@Component({
  selector: 'app-status-store',
  templateUrl: './status-store.component.html',
  styleUrls: ['./status-store.component.scss']
})
export class StatusStoreComponent implements OnInit {

  columns: ICosmoTableColumn[] = [
    { name: 'id', label: 'ID', field: '_id', canHide: false },
    { name: 'firstName', label: 'First Name', field: 'name.first',
      canSort: true },
    { name: 'lastName', label: 'Last Name', field: 'name.last',
      canSort: true },
    { name: 'email', label: 'Email', field: 'email', canSort: true },
    { name: 'guid', label: 'GUID', field: 'guid' },
  ];

  users: IUser[];

  storeAdapter: ICosmoTableStoreAdapter;

  savedStatus: ICosmoTablePersistableStatusSnapshot | null = null;

  restoredStatus: ICosmoTablePersistableStatusSnapshot | null = null;

  constructor(private userService: UserService) {

    this.users = this.userService.getUsers(100);

    this.storeAdapter = {
      save: (payload: ICosmoTableStoreAdapterSaveContext | null) => {
        this.restoredStatus = null;
        this.savedStatus = payload?.status || null;
        localStorage.setItem('statusStoreSample', JSON.stringify(payload?.status));
        return of(true);
      },
      load: () => {
        const saved = localStorage.getItem('statusStoreSample');
        if (saved) {
          this.savedStatus = null;
          this.restoredStatus = JSON.parse(saved);
          return of(this.restoredStatus);
        }
        return of(null);
      }
    };
  }

  ngOnInit(): void {
  }

}
