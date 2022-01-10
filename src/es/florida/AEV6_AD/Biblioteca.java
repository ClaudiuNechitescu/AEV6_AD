package es.florida.AEV6_AD;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class Biblioteca {
	/**
	 * Mètode main que mostra un menu i executa el mètode que tria l'usuari
	 * 
	 * @author Claudiu Andrei Nechitescu
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//Per a connectar-se a una BBDD d'un cluster remot
		MongoClient mongoClient = MongoClients.create("mongodb+srv://sa:-a123456@aev6.ovbdk.mongodb.net/test");
		//Per a connectar-se a una BBDD local
		//MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase database = mongoClient.getDatabase("Biblioteca");
		MongoCollection<Document> coleccion = database.getCollection("Llibres");
		//CRUD operations
		
		Scanner teclado = new Scanner(System.in);
		int opcio = 0;
		String id;

		while (opcio != 6) {
			// Obri una nova sessió de la session factory
			System.out.println("\n\n=============================================");
			System.out.println("              MENÚ BIBLIOTECA");
			System.out.println("=============================================");
			System.out.println("1. Mostrar tots els ids i els títols de la biblioteca.");
			System.out.println("2. Mostrar la informació detallada d’un llibre a partir del seu id.");
			System.out.println("3. Afegir un nou llibre a la biblioteca.");
			System.out.println("4. Modificar atributs d’un llibre a partir del seu id.");
			System.out.println("5. Esborrar un llibre a partir del seu id.");
			System.out.println("6. Tancar la biblioteca");
			System.out.print("\n >>> Tria una opció: ");
			opcio = Integer.parseInt(teclado.next());
			switch (opcio) {
			case 1:
				mostrarTitols(coleccion);
				break;
			case 2:
				mostrarTitols(coleccion);
				System.out.print(" >> Indica el nombre del llibre que vols mostrar: ");
				id = teclado.next();
				mostrarLlibre(coleccion, id);
				break;
			case 3:
				crearLlibre(coleccion);
				break;
			case 4:
				mostrarTitols(coleccion);
				System.out.print(" >> Indica el nombre del llibre a modificar: ");
				id = teclado.next();
				modificarLlibre(coleccion, id);
				break;
			case 5:
				mostrarTitols(coleccion);
				System.out.print(" >> Indica el nombre del llibre a esborrar: ");
				id = teclado.next();
				eliminarLlibre(coleccion, id);
				break;
			case 6:
				teclado.close();
				break;
			default:
				break;
			}
		}
		
		mongoClient.close();

	}
	/**
	 * Mètode que mostra la id i el titol de tots els llibres de la biblioteca
	 * 
	 * @param coleccion La col·lecció
	 * @author Claudiu Andrei Nechitescu
	 */
	public static void mostrarTitols(MongoCollection<Document> coleccion) {
		MongoCursor<Document> cursor = coleccion.find().sort(new BasicDBObject("Id",1)).iterator();
		while (cursor.hasNext()) {
			JSONObject obj = new JSONObject(cursor.next().toJson());
		System.out.println(obj.get("Id") + " - " + obj.get("Titol"));
		}
	}
	/**
	 * Mètode que mostra l'informació d'un llibre donada la seua id
	 * 
	 * @param coleccion La col·lecció
	 * @param id      La id del llibre
	 * @author Claudiu Andrei Nechitescu
	 */
	public static void mostrarLlibre(MongoCollection<Document> coleccion, String id) {
		Bson query = Filters.eq("Id",id);
		MongoCursor<Document> cursor = coleccion.find(query).iterator();
		while (cursor.hasNext()) {
			JSONObject obj = new JSONObject(cursor.next().toJson());
		System.out.println("Id: " + obj.get("Id") + "\nTitol: " + obj.get("Titol") + "\nAutor: " + obj.get("Autor") + "\nAny naixement: " + obj.get("Any_naixement") + "\nAny publicacio: " + obj.get("Any_publicacio") + "\nEditorial: " + obj.get("Editorial") + "\nNombre pagines: " + obj.get("Nombre_pagines"));
		System.out.println("=========================================================");
		}

	}
	/**
	 * Mètode que crea un llibre en la BBDD
	 * 
	 * @param coleccion La col·lecció
	 * @author Claudiu Andrei Nechitescu
	 */
	public static void crearLlibre(MongoCollection<Document> coleccion) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Introdueix el titol: ");
		String titol = sc.nextLine();
		System.out.println("Introdueix l'autor: ");
		String autor = sc.nextLine();
		System.out.println("Introdueix l'any de naixement de l'autor: ");
		String anyN = sc.nextLine();
		System.out.println("Introdueix l'any de publicació del llibre : ");
		String anyP = sc.nextLine();
		System.out.println("Introdueix l'editorial: ");
		String editorial = sc.nextLine();
		System.out.println("Introdueix el nombre de pagines: ");
		String nomP = sc.nextLine();

		Document doc = new Document();
		doc.append("Titol", titol);
		doc.append("Autor", autor);
		doc.append("Any_naixement", anyN);
		doc.append("Any_publicacio", anyP);
		doc.append("Editorial", editorial);
		doc.append("Nombre_pagines", nomP);
		coleccion.insertOne(doc);
	}
	/**
	 * Mètode que modifica l'informació d'un llibre donada la seua id
	 * 
	 * @param coleccion La col·lecció
	 * @param id      La id del llibre
	 * @author Claudiu Andrei Nechitescu
	 */
	public static void modificarLlibre(MongoCollection<Document> coleccion, String id) throws IOException {
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		int resposta;

		do {
			System.out.println(">> Què vols canviar?" + "\n1. Títol" + "\n2. Autor" + "\n3. Any de naixement" + "\n4. Any de publicació"
					+ "\n5. Editorial" + "\n6. Nombre de pàgines" + "\n7. Res");

			resposta = Integer.parseInt(br.readLine());
			switch (resposta) {
			case 1: {
				System.out.println("Introdueix el nou titol: ");
				String nou = br.readLine();
				coleccion.updateOne(Filters.eq("Id", id), new Document("$set",
						new Document("Titol", nou)));
				break;
			}
			case 2: {
				System.out.println("Introdueix el nou autor: ");
				String nou = br.readLine();
				coleccion.updateOne(Filters.eq("Id", id), new Document("$set",
						new Document("Autor", nou)));
				break;

			}
			case 3: {
				System.out.println("Introdueix el nou any de naixement: ");
				String nou = br.readLine();
				coleccion.updateOne(Filters.eq("Id", id), new Document("$set",
						new Document("Any_naixement", nou)));
				break;

			}
			case 4: {
				System.out.println("Introdueix el nou any de publicacio: ");
				String nou = br.readLine();
				coleccion.updateOne(Filters.eq("Id", id), new Document("$set",
						new Document("Any_publicacio", nou)));
				break;
			}
			case 5: {
				System.out.println("Introdueix la nova editorial: ");
				String nou = br.readLine();
				coleccion.updateOne(Filters.eq("Id", id), new Document("$set",
						new Document("Editorial", nou)));	
				break;

			}
			case 6: {
				System.out.println("Introdueix el nou nombre de pagines: ");
				String nou = br.readLine();
				coleccion.updateOne(Filters.eq("Id", id), new Document("$set",
						new Document("Nombre_pagines", nou)));
				break;
			}
			default: {
				System.out.println("Has d'introduir un numero de 1 a 7");
			}
			}
			resposta=resposta!=7?0:7;
		}while ((resposta > 7 || resposta < 1)&&resposta!=7);
		
		isr.close();
		br.close();
	}
	/**
	 * Mètode que esborra un llibre donada la seua id
	 * 
	 * @param coleccion La col·lecció
	 * @param id      La id del llibre
	 * @author Claudiu Andrei Nechitescu
	 */
	public static void eliminarLlibre(MongoCollection<Document> coleccion, String id) {
		coleccion.deleteOne(Filters.eq("Id", id));
	}
	
}
