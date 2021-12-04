import { Component, Input, OnInit } from '@angular/core'
import { MatTableDataSource } from '@angular/material/table'
import { StandardModel } from 'src/app/models/standard-model'

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent implements OnInit {
  constructor () {}

  dataSource: MatTableDataSource<StandardModel>

  @Input() config: {
    data: StandardModel[]
    columnMappings: { [key: string]: string }
    hideCrudButtons?: boolean
    edit: (item: StandardModel) => void
    delete: (item: StandardModel) => void
  }

  get columns () {
    return Object.keys(this.config.columnMappings).concat('Actions')
  }

  ngOnInit () {
    this.dataSource = new MatTableDataSource(this.config.data)
  }
}
