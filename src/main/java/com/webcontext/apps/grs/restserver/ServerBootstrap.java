/**
 * 
 */
package com.webcontext.apps.grs.restserver;

import com.webcontext.apps.grs.framework.repository.MongoDBConnection;
import com.webcontext.apps.grs.framework.repository.exception.NullMongoDBConnection;
import com.webcontext.apps.grs.framework.restserver.server.Bootstrap;
import com.webcontext.apps.grs.framework.restserver.server.IBootstrap;
import com.webcontext.apps.grs.models.Game;
import com.webcontext.apps.grs.repository.GameRepository;
import com.webcontext.apps.grs.service.DataManager;

/**
 * @author 212391884
 *
 */
@Bootstrap
public class ServerBootstrap implements IBootstrap {

	public void initialized() {
		MongoDBConnection connection = new MongoDBConnection();
		GameRepository gr = new GameRepository();
		gr.setConnection(connection);

		if (DataManager.countEntities(Game.class) != 0) {
			try {
				String game01 = "{ \"id\" : \"000001\", \"title\" : \"Watch Dogs\", \"cover\" : {  \"mini\" : \"http://ecx.images-amazon.com/images/I/91kXsI0XkFL._SL80_.jpg\",  \"medium\" : \"http://ecx.images-amazon.com/images/I/91kXsI0XkFL._SL120_.jpg\",  \"big\" : \"http://ecx.images-amazon.com/images/I/91kXsI0XkFL._SL240_.jpg\",  \"full\" : \"http://ecx.images-amazon.com/images/I/91kXsI0XkFL._SL1500_.jpg\" }, \"tags\" : [   \"gtalike\",   \"adventure\",   \"action\",   \"openworld\" ], \"platform\" : [   \"PS4\",   \"PS3\",   \"XONE\",   \"X360\",   \"PC\",   \"WiiU\" ], \"rate\" : {  \"gfx\" : 18,  \"sound\" : 15,  \"music\" : 16,  \"feeling\" : 18 }, \"content\" : {  \"fr\" : \"Watch Dogs est un jeu d'action à la troisième personne sur Xbox 360. Dans un univers moderne et ouvert où tout est connecté à un système de contrôle central appartenant à des sociétés privées, le joueur incarne un groupe de hackeurs et d'assassins capables de manipuler et de pirater les systèmes électroniques.\",  \"en\" : \"All it takes is the swipe of a finger. We connect with friends. We buy the latest gadgets and gear. We find out what’s happening in the world. But with that same simple swipe, we cast an increasingly expansive shadow. With each connection, we leave a digital trail that tracks our every move and milestone, our every like and dislike. And it’s not just people. Today, all major cities are networked. Urban infrastructures are monitored and controlled by complex operating systems.\" }, \"publicationDate\" : \"2014-05-27 00:00:00\", \"editor\" : \"Ubisoft\", \"developer\" : \"Ubisoft Montreal\", \"rated\" : \"+18\"}";
				Game game = gr.convertToObject(game01);
				gr.save(game);
			} catch (NullMongoDBConnection e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
