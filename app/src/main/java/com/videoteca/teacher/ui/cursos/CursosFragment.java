package com.videoteca.teacher.ui.cursos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.videoteca.teacher.ui.curso.CursoActivity;
import com.videoteca.teacher.R;
//import com.videoteca.teacher.data.model.Curso;
import com.videoteca.teacher.data.model.realm.CursoData;
import com.videoteca.teacher.data.model.realm.SessionData;
import com.videoteca.teacher.data.model.retrofit.Curso;
import com.videoteca.teacher.data.model.retrofit.RespCursos;
import com.videoteca.teacher.managers.SessionManager;
import com.videoteca.teacher.ui.adapters.CursoAdapter;
import com.videoteca.teacher.ui.home.ShareHomeDataViewModel;

import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class CursosFragment extends Fragment {

    private CursosViewModel cursosViewModel;

    private RecyclerView listRecyclerView;
    private CursoAdapter cursoAdapter;
    //private ArrayList<Curso> cursos;
    private RealmList<CursoData> cursos;
    private Context context;
    //Session manager
    private SessionManager sessionManager;
    //Start Realm Object
    private Realm realm;
    private String authorization;
    private ShareHomeDataViewModel shareHomeDataViewModel;
    private RealmResults cursoResults;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //cursosViewModel = new ViewModelProvider(this).get(CursosViewModel.class);
        // View Model
        cursosViewModel = new ViewModelProvider(this, new CursosViewModelFactory()).get(CursosViewModel.class);
        View root = inflater.inflate(R.layout.fragment_cursos, container, false);
        shareHomeDataViewModel = new ViewModelProvider(requireActivity()).get(ShareHomeDataViewModel.class);
        /*final TextView textView = root.findViewById(R.id.text_gallery);
        cursosViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        listRecyclerView = root.findViewById(R.id.list_recycler_view);

        // sections = prepareData();
        //cursos = new ArrayList<>();
        //cursos = prepareData();

        context = Objects.requireNonNull(requireActivity()).getApplicationContext();

        // Instance Session Manager
        sessionManager = new SessionManager(context);

        //Get realm instance
        realm = Realm.getDefaultInstance();
        cursos = new RealmList<>();
        cursoAdapter = new CursoAdapter(context, cursos);
        cursoAdapter.setOnItemClickListener(new CursoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CursoData cursoData) {
                Toast.makeText(context, cursoData.getNombre(), Toast.LENGTH_LONG).show();

                //Gson gson = new Gson();
                String cursoJSON = new Gson().toJson(cursoData);

                Intent intent = new Intent(context, CursoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("CURSO_JSON", cursoJSON);
                context.startActivity(intent);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        listRecyclerView.setLayoutManager(linearLayoutManager);
        listRecyclerView.setAdapter(cursoAdapter);

        if (sessionManager.isLoggedIn()){
            //Get data session Realm
            SessionData sessionData = realm.where(SessionData.class).findFirst();
            Log.i("Curso User Name", sessionData.getNombre().concat(" ").concat(sessionData.getPrimer_ap()).concat(" ").concat(sessionData.getSegundo_ap()));
            Log.i("Curso User Email", sessionData.getEmail());
            authorization = sessionData.getToken();
            Log.i("Curso Authorization", authorization);
        } else {
            Log.e("Curso estado de sesion", sessionManager.isLoggedIn()+"");
            RealmQuery<SessionData> querySesDa = realm.where(SessionData.class);
            RealmResults<SessionData> resultSesDa = querySesDa.findAll();
            Log.e("Curso Realm Session Data Query", resultSesDa.toString());
        }

        // Recuperando los cursos
        RealmQuery<CursoData> queryCursos = realm.where(CursoData.class);
        cursoResults = queryCursos.findAll();

        if (cursoResults.size() > 0){
            Log.e("Cursos Results Size", cursoResults.size()+"");
            Log.e("cursos.size()", cursos.size()+"");
            List<CursoData> cursosAux = realm.copyFromRealm(cursoResults);
            cursos.addAll(cursosAux);
        }

        shareHomeDataViewModel.getSessionDataMutableLiveData().observe(requireActivity(), new Observer<SessionData>() {
            @Override
            public void onChanged(SessionData sessionData) {
                Log.e("Share Curso User Name", sessionData.getNombre().concat(" ").concat(sessionData.getPrimer_ap()).concat(" ").concat(sessionData.getSegundo_ap()));
                Log.e("Share Curso User Email", sessionData.getEmail());
                Log.i("Share Curso Authorization", sessionData.getToken());
                if (cursoResults.size() == 0) {
                    cursosViewModel.getCursos(requireActivity(), sessionData.getToken());
                } /*else {
                    Log.e("Share Cursos Results Size", cursoResults.size()+"");
                    Log.e("cursos.size()", cursos.size()+"");
                    List<CursoData> cursosAux = realm.copyFromRealm(cursoResults);
                    cursos.addAll(cursosAux);
                    Log.e("cursos.size() LOAD", cursos.size()+"");
                }*/
                // Cursos View Model
                //cursosViewModel.getCursos(requireActivity(), sessionData.getToken());
            }
        });

        cursosViewModel.getCursosData().observe(requireActivity(), new Observer<RespCursos>() {
            @Override
            public void onChanged(RespCursos respCursos) {
                Log.e("Nombre Curso 0: ", respCursos.getData().get(0).getNombre());
                Log.e("cursoResults.size()", cursoResults.size()+"");
                Log.e("cursos.size()", cursos.size()+"");
                prepareCursos(respCursos);
                /*if (cursoResults.size() == 0) {
                    prepareCursos(respCursos);
                }*/
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
    }

    private void prepareCursos(RespCursos respCursos) {

        // Comprobando registro de cursos
        if (cursoResults.size() > 0) {
            realm.beginTransaction();
            realm.delete(CursoData.class);
            realm.commitTransaction();

            cursoResults.deleteAllFromRealm();
        }

        if (cursos.size() > 0) {
            cursos.clear();
            cursoAdapter.notifyDataSetChanged();
        }

        List<Curso> cursoList = respCursos.getData();

        realm.beginTransaction();

        RealmList<CursoData> cursoDataX = new RealmList<>();
        for (Curso curso: cursoList) {
            Log.i("Curso ID", String.valueOf(curso.getId()));
            Log.i("Curso Nombre", curso.getNombre());
            cursoDataX.add(new CursoData(curso.getId(), curso.getNombre(), curso.getDescripcion(), curso.getEstado(), curso.getCreatedAt()));
        }

        realm.copyToRealmOrUpdate(cursoDataX);
        realm.commitTransaction();

        cursos.addAll(cursoDataX);
        cursoAdapter.notifyDataSetChanged();

    }

    /*private ArrayList<Curso> prepareData() {
        ArrayList<Curso> cursos = new ArrayList<>();

        new Curso(1, "Primero de Secundaria A - matematica", "Curso de Primero de secundaria A de la asignatura de matematica", true,1);

        cursos.add(new Curso(1, "Primero de Secundaria A - matematica", "Curso de Primero de secundaria A de la asignatura de matematica", true,1));
        cursos.add(new Curso(2, "Segundo de Secundaria B - matematica", "Curso de Segundo de secundaria B de la asignatura de matematica", true,1));
        cursos.add(new Curso(3, "Tercero de Secundaria D - matematica", "Curso de Tercero de secundaria D de la asignatura de matematica", true,1));

        return cursos;
    }*/
}