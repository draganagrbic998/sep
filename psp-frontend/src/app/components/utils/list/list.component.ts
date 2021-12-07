import { Component, Input, OnInit } from '@angular/core'
import { MatDialog, MatDialogConfig } from '@angular/material/dialog'
import { MatTableDataSource } from '@angular/material/table'
import { Router } from '@angular/router'
import { StandardModel } from 'src/app/models/standard-model'
import { StandardRestService } from 'src/app/services/standard-rest.service'
import { DIALOG_CONFIG } from 'src/app/utils/popup'
import { DeleteConfirmationComponent } from '../delete-confirmation/delete-confirmation.component'

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent implements OnInit {
  constructor(private dialog: MatDialog, private router: Router) { }

  dataSource: MatTableDataSource<StandardModel>

  @Input() config: {
    service: StandardRestService<StandardModel>
    columnMappings: { [key: string]: string }
    hideCreate?: boolean
    hideEdit?: boolean
    hideDelete?: boolean
    editRoute?: string
  }

  get columns() {
    return Object.keys(this.config.columnMappings).concat('Actions')
  }

  ngOnInit() {
    this.readData()
  }

  create() {
    this.router.navigate([`${this.config.editRoute}/new`])
  }

  edit(item: StandardModel) {
    this.router.navigate([`${this.config.editRoute}/${item.id}`])
  }

  async delete(item: StandardModel) {
    const options: MatDialogConfig = {
      ...DIALOG_CONFIG,
      ...{ data: () => this.config.service.delete(item.id) }
    }
    const res = await this.dialog
      .open(DeleteConfirmationComponent, options)
      .afterClosed()
      .toPromise()
    if (res) {
      this.readData()
    }
  }

  private async readData() {
    this.dataSource = new MatTableDataSource(await this.config.service.read().toPromise())
  }
}
