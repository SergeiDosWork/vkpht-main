package me.goodt.vkpht.module.orgstructure.config;

import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentDto;
import me.goodt.vkpht.module.orgstructure.application.impl.AssignmentServiceImpl;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamAssignmentEntity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import me.goodt.vkpht.common.application.util.ExpiredConcurrentHashMap;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Configuration
public class CacheConfig {
    @Value("${appConfig.cache.data.expiredInMillis}")
    private long expiredInMillis;
    @Value("${appConfig.cache.data.cleanerInMillis}")
    private long cleanerInMillis;

    @Bean("assignments")
    public AssignmentCache getAssignmentsCache() {
        return new AssignmentCache("assignments", expiredInMillis, cleanerInMillis);
    }

    @Bean("employeeAssignments")
    public EmployeeAssignmentCache getEmployeeAssignmentsCache() {
        return new EmployeeAssignmentCache("employeeAssignments", expiredInMillis, cleanerInMillis);
    }

    public static class AssignmentCache extends ExpiredConcurrentHashMap<DivisionTeamAssignmentKey, List<DivisionTeamAssignmentEntity>> {
        public AssignmentCache(String cacheName, long expiryInMillis, long cleanerInMillis) {
            super(cacheName, expiryInMillis, cleanerInMillis);
        }
    }

    public static class EmployeeAssignmentCache extends ExpiredConcurrentHashMap<Long, DivisionTeamAssignmentDto> {
        public EmployeeAssignmentCache(String cacheName, long expiryInMillis, long cleanerInMillis) {
            super(cacheName, expiryInMillis, cleanerInMillis);
        }
    }

    public static class DivisionTeamAssignmentKey {
        List<Long> ids;
        List<Long> employeeIds;
        Long divisionTeamId;
        Boolean withClosed;

        public DivisionTeamAssignmentKey(List<Long> ids, List<Long> employeeIds, Long divisionTeamId, Boolean withClosed) {
            this.ids = ids;
            this.employeeIds = employeeIds;
            this.divisionTeamId = divisionTeamId;
            this.withClosed = withClosed;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof DivisionTeamAssignmentKey)) {
                return false;
            }
            DivisionTeamAssignmentKey other = (DivisionTeamAssignmentKey) o;
            boolean idsEquals = this.ids == other.ids || ((this.ids != null && other.ids != null)
                && equalsIgnoreOrder(this.ids, other.ids));
            boolean employeeIdsEquals = this.employeeIds == other.employeeIds || ((this.employeeIds != null && other.employeeIds != null)
                && equalsIgnoreOrder(this.employeeIds, other.employeeIds));
            return idsEquals && employeeIdsEquals && Objects.equals(other.divisionTeamId, this.divisionTeamId) &&
                Objects.equals(this.withClosed, other.withClosed);
        }

        @Override
        public final int hashCode() {
            int result = 17;
            if (ids != null) {
                result = 31 * result + ids.hashCode();
            }
            if (employeeIds != null) {
                result = 31 * result + employeeIds.hashCode();
            }
            if (divisionTeamId != null) {
                result = 31 * result + divisionTeamId.hashCode();
            }
            if (withClosed != null) {
                result = 31 * result + withClosed.hashCode();
            }
            return result;
        }

        private boolean equalsIgnoreOrder(List<Long> l1, List<Long> l2) {
            return new HashSet<>(l1).equals(new HashSet<>(l2));
        }
    }
}
