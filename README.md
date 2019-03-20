# TO DO
* Comenzar a programar las clases nuevas!


# Readme original

## Objetivo
Crear un guerrero para matar a los otros.

## Acciones
* Action: Las acciones que se pueden realizar en un turno
* Attack: Metodo que recibe una celda a la cual se va a atacar.
* Move: Hay que crear una clase que extienda a Move, y en ella implementar los metodos de movimiento.
* Skip: pasa el turno
* Suicide: hace daño segun la distancia del enemigo y cuanta vida tengamos.


## Eventos  
* Las paredes se van achicando(Muerte subita), y te pueden aplastar, o te acercan al hunter. 


## Metodos:
* Warrior:
	* playTurn(): Metodo que toma un "tick" y el numero de accion, aca adentro va todo lo que el warrior va a ejecutar en su turno.
	* wasAttacked(): Metodo que informa cuanto daño nos hicieron y desde que celda. Se puede utilizar para reaccionar ante un ataque enemigo.
	* enemyKilled(): Metodo que se ejecuta al matar a un enemigo.
	* worldChanged(): Metodo que se ejecuta cuando el mundo ha cambiado. ( En algunas versiones del BattleField-AI el mundo cambia dinamicamente.)
	* getInRange(): Para ver si estamos en rango.
	* getPosition(): Donde estamos.
* Battlefield:
	* getSpecialItems(): Devuelve la lista de que cajitas podemos ver.
	* getHunterData(): Podemos detectar donde esta el hunter.
	* getEnemyData(): Detecta la informacion del enemigo.
* ConfigurationManager:
	* getMaxWarriorPerBattle(): Cuantos guerreros podemos enviar a la batalla.
	* getMaxRangeForWarrior(): El rango maximo de ataque.
	* getFieldCellHitPoints(): Obtener los hitpoints de la celda.

## Clases
* Warrior: Clase que compone a los guerreros.
* ConfigurationManager: Es una clase que no podemos editar. De ella podemos obtener diversos metodos para obtener informacion en el juego.
* WarriorManager: Es el manager, en esta clase se decide que guerrero vamos a enviar a la batalla y como distribuir sus distintas caracteristicas. Tambien podemos acceder al alto, ancho del mapa. Cuanto falta para la "Muerte Subita"; La cantidad maxima de puntos a distribuir, etc
* FieldCell: representa cada una de las celdas del mapa.


## Conceptos
* Ataque: En el ataque influye la distancia a la que se encuentra el enemigo, nuestra fuerza y la defensa del enemigo.
* Movimiento: Si la cantidad de celdas excede lo que podemos caminar, no se realiza el movimiento. Moverse en diagonal cuesta 1.41 puntos de movimiento. El terreno pantanoso hace que nos movamos mas despacio. (La velocidad que se coloca en el personaje se divide por 5, y esa es al cantidad de celdas que puede caminar).
* ExecutorService: Si el guerrero se encuentra mas de dos segundos inactivos, el mismo será asesinado por time out.

> PRECAUCION: Hay algunos metodos que estan prohibidos, por mas que sean publicos. EJ: addWarriorManager, isWarriorInRange, getWarriors, etc..

## Requisitos para un trabajo aprobado

2. 1. Por lo menos un algoritmo de busqueda
2. > Y Alguna logica de las acciones del warrior, como el warrior manager.

## Como Exportar para intgracion

> Seleccionamos nuestro proyecto con nuestros guerreros con **boton derecho** > Export > JAVA > JAR File

Luego ejecutamos el BattleField-IA, el proyecto principal y cargamos nuestros .jar

## Instalacion

1. Crear la carpeta del proyecto
2. Bajar el Battlefield-AI
3. Crear un proyecto e importar Battlefield-AI
4. Crear un guerrero que extienda la clase Warrior y otra clase que extienda a WarriorManager
5. Programar!


Agradecimiento: Jaime por el documento.
