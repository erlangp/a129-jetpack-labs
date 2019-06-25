package com.dicoding.academies.ui.reader;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.dicoding.academies.data.source.AcademyRepository;
import com.dicoding.academies.data.source.local.entity.ModuleEntity;
import com.dicoding.academies.vo.Resource;

import java.util.List;

import javax.inject.Inject;


public class CourseReaderViewModel extends ViewModel {

    private MutableLiveData<String> courseId = new MutableLiveData<>();
    private MutableLiveData<String> moduleId = new MutableLiveData<>();
    private AcademyRepository academyRepository;
    public LiveData<Resource<List<ModuleEntity>>> modules = Transformations.switchMap(courseId,
            mCourseId -> academyRepository.getAllModulesByCourse(mCourseId));
    public LiveData<Resource<ModuleEntity>> selectedModule = Transformations.switchMap(moduleId,
            selectedPosition -> academyRepository.getContent(selectedPosition)
    );

    @Inject
    public CourseReaderViewModel(AcademyRepository mAcademyRepository) {
        this.academyRepository = mAcademyRepository;
    }

    public void setCourseId(String courseId) {
        this.courseId.setValue(courseId);
    }

    public void setSelectedModule(String moduleId) {
        this.moduleId.setValue(moduleId);
    }

    public void readContent(ModuleEntity module) {
        academyRepository.setReadModule(module);
    }

    public int getModuleSize() {
        if (modules.getValue() != null) {
            List<ModuleEntity> moduleEntities = modules.getValue().data;
            if (moduleEntities != null) {
                return moduleEntities.size();
            }
        }
        return 0;
    }

    public void setNextPage() {
        if (selectedModule.getValue() != null && modules.getValue() != null) {
            ModuleEntity moduleEntity = selectedModule.getValue().data;
            List<ModuleEntity> moduleEntities = modules.getValue().data;
            if (moduleEntity != null && moduleEntities != null) {
                int position = moduleEntity.getPosition();
                if (position < moduleEntities.size() && position >= 0) {
                    setSelectedModule(moduleEntities.get(position + 1).getModuleId());
                }
            }
        }
    }

    public void setPrevPage() {
        if (selectedModule.getValue() != null && modules.getValue() != null) {
            ModuleEntity moduleEntity = selectedModule.getValue().data;
            List<ModuleEntity> moduleEntities = modules.getValue().data;
            if (moduleEntity != null && moduleEntities != null) {
                int position = moduleEntity.getPosition();
                if (position < moduleEntities.size() && position >= 0) {
                    setSelectedModule(moduleEntities.get(position - 1).getModuleId());
                }
            }
        }
    }
}