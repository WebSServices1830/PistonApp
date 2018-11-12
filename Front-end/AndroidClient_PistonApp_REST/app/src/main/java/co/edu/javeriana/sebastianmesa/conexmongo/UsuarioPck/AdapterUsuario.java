package co.edu.javeriana.sebastianmesa.conexmongo.UsuarioPck;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import co.edu.javeriana.sebastianmesa.conexmongo.CartaMenu;
import co.edu.javeriana.sebastianmesa.conexmongo.R;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class AdapterUsuario extends RecyclerView.Adapter<AdapterUsuario.MyViewHolder> {

    private Context mContext;
    private List<CartaMenu> cartaMenuList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }


    }


    public AdapterUsuario(Context mContext, List<CartaMenu> cartaMenuList) {
        this.mContext = mContext;
        this.cartaMenuList = cartaMenuList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        CartaMenu cartaMenu = cartaMenuList.get(position);
        holder.title.setText(cartaMenu.getName());
        holder.count.setText(cartaMenu.getDescripcion());

        // loading cartaMenu cover using Glide library
        Glide.with(mContext).load(cartaMenu.getThumbnail()).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, "Seleccion "+position, Toast.LENGTH_LONG).show();
                if (position == 0){
                    Intent intent = new Intent(mContext, CrearUsuarioView.class);
                    mContext.startActivity(intent);
                }
                if (position == 1){
                    Intent intent = new Intent(mContext, VerUsuarioView.class);
                    mContext.startActivity(intent);
                }
                if (position == 2){
                    Intent intent = new Intent(mContext, ActualizarUsuarioView.class);
                    mContext.startActivity(intent);
                }
                if (position == 3){
                    Intent intent = new Intent(mContext, EliminarUsuarioView.class);
                    mContext.startActivity(intent);
                }

            }
        });
    }



    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        //inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return cartaMenuList.size();
    }


}