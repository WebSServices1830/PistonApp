export class Piloto {
    ID: number;
    nombre: string;
    fecha_naciemiento: Date;
    foto: string;
    constructor(ID, nombre, fecha_nacimiento, foto){
        this.ID=ID;
        this.nombre=nombre;
        this.fecha_naciemiento= fecha_nacimiento;
        this.foto= foto;
    }
}
