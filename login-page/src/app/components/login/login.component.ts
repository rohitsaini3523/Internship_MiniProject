import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Credentials } from '../../model/credentials';
import { BackendService } from '../../services/backend/backend.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  credentials!: Credentials;
  loginForm: FormGroup; 
  submitted = false;

  constructor(private fb: FormBuilder, private backend_service: BackendService) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]]
    });
  }
  get formControls() { 
    return this.loginForm.controls; 
  }

  login() {
    this.submitted = true; 

    if (this.loginForm.invalid) {
      return;
    }

    this.credentials = new Credentials();
    this.credentials.username = this.loginForm.get("username")?.value;
    this.credentials.password = this.loginForm.get("password")?.value;
    this.backend_service.newLogin(this.credentials);
  }
}
