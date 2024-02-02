import { Injectable } from '@angular/core';
import { Credentials } from '../../model/credentials';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class BackendService {

  constructor(private http:HttpClient) { }

  newLogin(credentials: Credentials) {
    this.http.post("enter url here", credentials,{}).subscribe({next:(Response)=>{
      console.log(Response);} ,
    
      error:(show_error)=>{
        console.log(show_error);}
      
    
    });
    console.log(credentials)
  }
}
