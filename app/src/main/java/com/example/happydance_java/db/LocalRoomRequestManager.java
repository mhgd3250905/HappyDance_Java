package com.example.happydance_java.db;

import android.content.Context;
import android.os.AsyncTask;

import com.example.happydance_java.db.dao.PositionDao;

import java.util.List;

import androidx.lifecycle.LiveData;

public class LocalRoomRequestManager {
    private volatile static LocalRoomRequestManager INSTANCE;

    public static LocalRoomRequestManager getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LocalRoomRequestManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LocalRoomRequestManager(context);
                }
            }
        }
        return INSTANCE;
    }

    private LiveData<List<Position>> positionsLive;
    private PositionDao positionDao;

    LocalRoomRequestManager(Context context) {
        AppDataBase appDataBase = AppDataBase.getInstance(context.getApplicationContext());
        positionDao = appDataBase.getPositionDao();
        positionsLive = positionDao.queryAll();
    }

    public LiveData<List<Position>> getPositionsLive() {
        return positionsLive;
    }

    public void insertPositions(Position... positions) {
        new InsertAsyncTask(positionDao).execute(positions);
    }

    public void updatePositions(Position... positions) {
        new UpdateAsyncTask(positionDao).execute(positions);
    }

    public void deletePositions(Position... positions) {
        new DeleteAsyncTask(positionDao).execute(positions);
    }

    public void deleteAllPositions() {
        new DeleteAllAsyncTask(positionDao).execute();
    }

    class InsertAsyncTask extends AsyncTask<Position, Void, Void> {
        private PositionDao dao;

        public InsertAsyncTask(PositionDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Position... positions) {
            dao.insert(positions);
            return null;
        }
    }

    class UpdateAsyncTask extends AsyncTask<Position, Void, Void> {
        private PositionDao dao;

        public UpdateAsyncTask(PositionDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Position... positions) {
            dao.update(positions);
            return null;
        }
    }

    class DeleteAsyncTask extends AsyncTask<Position, Void, Void> {
        private PositionDao dao;

        public DeleteAsyncTask(PositionDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Position... positions) {
            dao.delete(positions);
            return null;
        }
    }

    class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private PositionDao dao;

        public DeleteAllAsyncTask(PositionDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAll();
            return null;
        }
    }


}
