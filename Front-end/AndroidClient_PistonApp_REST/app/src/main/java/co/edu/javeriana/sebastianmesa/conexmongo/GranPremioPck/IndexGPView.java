package co.edu.javeriana.sebastianmesa.conexmongo.GranPremioPck;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.sebastianmesa.conexmongo.CartaMenu;
import co.edu.javeriana.sebastianmesa.conexmongo.EscuderiaPck.AdapterEscuderia;
import co.edu.javeriana.sebastianmesa.conexmongo.R;

public class IndexGPView extends AppCompatActivity {


    //Static Context
    public static Context context;
    public View theview;
    private Toolbar mTopToolbar;
    private RecyclerView recyclerView;
    private AdapterGP adapterGP;
    private List<CartaMenu> cartaMenuList;
    private TextView campoDeSubtitulo;
    private ImageView fondo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getBaseContext();
        setContentView(R.layout.activity_index_gpview);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Set the status bar to dark-semi-transparentish
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }

        fondo = (ImageView) findViewById(R.id.backdrop);



        int sdk = Build.VERSION.SDK_INT;
        if (sdk < Build.VERSION_CODES.LOLLIPOP) {
            //fondo.setBackgroundColor(getResources().getColor(R.color.bg_screen1));
            try {
                findViewById(R.id.backdrop).setBackground(ContextCompat.getDrawable(context,R.drawable.gradiente_back2));
            }catch (Exception e){
                Log.e("ERRORACK", "Es: "+ e + " -> " +sdk);
                fondo.setBackgroundColor(getResources().getColor(R.color.bg_screen1));
            }

        } else {
            // in your gradient use android:gradientRadius="80%p" or
            // android:gradientRadius="200dp"
            try {
                findViewById(R.id.backdrop).setBackground(ContextCompat.getDrawable(context,R.drawable.gradiente_back2));
            }catch (Exception e){
                Log.e("ERRORACK", "Es: "+ e + " -> " +sdk);
                fondo.setBackgroundColor(getResources().getColor(R.color.bg_screen1));
            }

        }

        campoDeSubtitulo = (TextView) findViewById(R.id.subtitleTxt);

        campoDeSubtitulo.setText("Tu mejor compañia para la F1");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        initCollapsingToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        cartaMenuList = new ArrayList<>();
        adapterGP = new AdapterGP(this, cartaMenuList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new IndexGPView.GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterGP);

        prepareAlbums();

        try {
            //Glide.with(this).load(R.drawable.ic_logo).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

//    public void logout (){
//        FirebaseAuth.getInstance().signOut(); //signout firebase
//        Intent setupIntent = new Intent(getBaseContext(), LoginView.class);
//        Toast.makeText(getBaseContext(), "Has cerrado sesión. 👋", Toast.LENGTH_LONG).show(); //if u want to show some text
//        setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(setupIntent);
//        finish();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.menuAbout:
                //startActivity(new Intent(getBaseContext(), AboutView.class));
                break;

            case R.id.menuLogout:
                //logout();
                break;

        }
        return true;
    }


    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {
        int[] covers = new int[]{
                R.drawable.ic_gp,
                R.drawable.ic_gp,
                R.drawable.ic_gp,
                R.drawable.ic_gp};

        CartaMenu a = new CartaMenu("Crear GP", "Crea una GP", covers[0]);
        cartaMenuList.add(a);

        a = new CartaMenu("Ver GP", "Mira una GP", covers[1]);
        cartaMenuList.add(a);

        a = new CartaMenu("Actualizar GP", "Actualiza un GP", covers[2]);
        cartaMenuList.add(a);

        a = new CartaMenu("Eliminar GP", "Elimina un GP", covers[3]);
        cartaMenuList.add(a);


        adapterGP.notifyDataSetChanged();
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
