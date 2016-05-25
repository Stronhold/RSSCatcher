package es.deusto.model.services.database.dao;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import es.deusto.model.services.database.dao.Noticia;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "NOTICIA".
*/
public class NoticiaDao extends AbstractDao<Noticia, Long> {

    public static final String TABLENAME = "NOTICIA";

    /**
     * Properties of entity Noticia.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property NoticiaID = new Property(1, Long.class, "noticiaID", false, "NOTICIA_ID");
        public final static Property Titulo = new Property(2, String.class, "titulo", false, "TITULO");
        public final static Property Descripcion = new Property(3, String.class, "descripcion", false, "DESCRIPCION");
        public final static Property Image = new Property(4, String.class, "image", false, "IMAGE");
        public final static Property Link = new Property(5, String.class, "link", false, "LINK");
        public final static Property RssID = new Property(6, Long.class, "rssID", false, "RSS_ID");
    };

    private DaoSession daoSession;

    private Query<Noticia> rSS_NoticiaListQuery;

    public NoticiaDao(DaoConfig config) {
        super(config);
    }
    
    public NoticiaDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"NOTICIA\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"NOTICIA_ID\" INTEGER," + // 1: noticiaID
                "\"TITULO\" TEXT," + // 2: titulo
                "\"DESCRIPCION\" TEXT," + // 3: descripcion
                "\"IMAGE\" TEXT," + // 4: image
                "\"LINK\" TEXT," + // 5: link
                "\"RSS_ID\" INTEGER);"); // 6: rssID
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"NOTICIA\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Noticia entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long noticiaID = entity.getNoticiaID();
        if (noticiaID != null) {
            stmt.bindLong(2, noticiaID);
        }
 
        String titulo = entity.getTitulo();
        if (titulo != null) {
            stmt.bindString(3, titulo);
        }
 
        String descripcion = entity.getDescripcion();
        if (descripcion != null) {
            stmt.bindString(4, descripcion);
        }
 
        String image = entity.getImage();
        if (image != null) {
            stmt.bindString(5, image);
        }
 
        String link = entity.getLink();
        if (link != null) {
            stmt.bindString(6, link);
        }
    }

    @Override
    protected void attachEntity(Noticia entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Noticia readEntity(Cursor cursor, int offset) {
        Noticia entity = new Noticia( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // noticiaID
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // titulo
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // descripcion
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // image
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // link
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Noticia entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setNoticiaID(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setTitulo(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setDescripcion(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setImage(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setLink(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Noticia entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Noticia entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "noticiaList" to-many relationship of RSS. */
    public List<Noticia> _queryRSS_NoticiaList(Long rssID) {
        synchronized (this) {
            if (rSS_NoticiaListQuery == null) {
                QueryBuilder<Noticia> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.RssID.eq(null));
                rSS_NoticiaListQuery = queryBuilder.build();
            }
        }
        Query<Noticia> query = rSS_NoticiaListQuery.forCurrentThread();
        query.setParameter(0, rssID);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getRSSDao().getAllColumns());
            builder.append(" FROM NOTICIA T");
            builder.append(" LEFT JOIN RSS T0 ON T.\"NOTICIA_ID\"=T0.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Noticia loadCurrentDeep(Cursor cursor, boolean lock) {
        Noticia entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        RSS rSS = loadCurrentOther(daoSession.getRSSDao(), cursor, offset);
        entity.setRSS(rSS);

        return entity;    
    }

    public Noticia loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<Noticia> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Noticia> list = new ArrayList<Noticia>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<Noticia> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Noticia> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
