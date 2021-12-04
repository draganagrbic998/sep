import { Component, OnInit } from '@angular/core'
import { WebShop } from 'src/app/models/webshop'
import { WebshopService } from 'src/app/services/webshop.service'

@Component({
  selector: 'app-webshop-list',
  templateUrl: './webshop-list.component.html',
  styleUrls: ['./webshop-list.component.scss']
})
export class WebshopListComponent implements OnInit {
  constructor (private webshopService: WebshopService) {}

  data: WebShop[]

  columnMappings: { [key: string]: string } = {
    name: 'Shop Name',
    url: 'Shop Site'
  }

  edit(webshop: WebShop){
    alert('EDIT')
  }

  delete(webshop: WebShop){
    alert('DELETE')
  }

  ngOnInit () {
    this.readWebshops()
  }

  private async readWebshops () {
    this.data = await this.webshopService.read().toPromise()
  }
}
