package org.example.kerjaBelajar.BaseMapper;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
/**
public abstract class BaseMapper <SOURCE, TARGET> implements IBaseMapper<SOURCE, TARGET> {


		@Getter
		protected boolean isBatch = false;

		protected void prepareData(List<SOURCE> sources) {
		}

		@Override
		public List<TARGET> mapEntitiesIntoDTOs(Iterable<SOURCE> entities) {
			List<SOURCE> sources = new ArrayList<>();
			entities.forEach(sources::add);
			prepareData(sources);
			this.isBatch = true;
			List<TARGET> listTarget = new ArrayList<>();
			for (SOURCE source : entities) {
				listTarget.add(convert(source));
			}
			this.isBatch = false;
			return listTarget;
		}

		@Override
		public Page<TARGET> mapEntityPageIntoDTOPage(Pageable pageRequest, Page<SOURCE> source) {
			List<TARGET> targetList = mapEntitiesIntoDTOs(source.getContent());
			return new PageImpl<>(targetList, pageRequest, source.getTotalElements());
		}

		@Override
		public Slice<TARGET> mapEntitySliceIntoDTOSlice(Slice<SOURCE> source) {
			List<TARGET> targetList = mapEntitiesIntoDTOs(source.getContent());
			return new SliceImpl<>(targetList);
		}

		@Override
		public SOURCE convertReverse(TARGET p_TARGET) {
			return null;
		}

		@Override
		public ConvertResponseEntityDTO convertWithResponseEntity(SOURCE source) {
			return null;
		}

		@Override
		public TARGET convert(SOURCE source) {
			return null;
		}
	}

}
*/