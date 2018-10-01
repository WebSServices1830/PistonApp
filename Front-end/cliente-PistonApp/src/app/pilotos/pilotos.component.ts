import { Component, OnInit } from '@angular/core';
import { Piloto } from '../piloto';

@Component({
  selector: 'app-pilotos',
  templateUrl: './pilotos.component.html',
  styleUrls: ['./pilotos.component.css']
})
export class PilotosComponent implements OnInit {
pilotos = [
 new Piloto(1, 'esteban',new Date(), 'holaa')
];

  constructor() { }

  ngOnInit() {
  }

}
