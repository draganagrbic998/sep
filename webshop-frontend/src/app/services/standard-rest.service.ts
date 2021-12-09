import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { StandardModel } from '../models/standard-model';

export abstract class StandardRestService<T extends StandardModel> {

  constructor(
    protected http: HttpClient,
    protected entity: string
  ) {
    this.API_URL = `${environment.apiUrl}/${entity}`
  }

  protected API_URL: string;

  read() {
    return this.http.get<T[]>(this.API_URL);
  }

  readOne(id: number) {
    return this.http.get<T>(`${this.API_URL}/${id}`)
  }

  save(item: T) {
    if (item.id) {
      return this.http.put<T>(`${this.API_URL}/${item.id}`, item)
    }
    return this.http.post<T>(this.API_URL, item)
  }

  delete(id: number) {
    return this.http.delete<void>(`${this.API_URL}/${id}`)
  }

}
