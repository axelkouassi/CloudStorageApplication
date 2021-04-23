package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) " +
            "VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insert (Notes note);

    @Update("UPDATE NOTES set notetitle= #{noteTitle}, notedescription = #{noteDescription}, " +
            "userid = #{userId} WHERE noteid = #{noteId}")
    int update(Notes note);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteId}")
    void delete(Integer noteId);

    @Select("SELECT * FROM NOTES")
    List<Notes> getAllNotes();

    @Select("SELECT * FROM NOTES WHERE noteid = #{noteId}")
    List<Notes> getNoteByNoteId(Integer noteId);

    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    List<Notes> getNotesForUser(Integer userId);


}
