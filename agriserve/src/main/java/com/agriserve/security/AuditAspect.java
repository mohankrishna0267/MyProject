package com.agriserve.security;

import com.agriserve.entity.AuditLog;
import com.agriserve.entity.User;
import com.agriserve.repository.AuditLogRepository;
import com.agriserve.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    @Pointcut("execution(* com.agriserve.service.*Service.create*(..)) || " +
              "execution(* com.agriserve.service.*Service.register*(..)) || " +
              "execution(* com.agriserve.service.*Service.upload*(..)) || " +
              "execution(* com.agriserve.service.*Service.submit*(..)) || " +
              "execution(* com.agriserve.service.*Service.schedule*(..))")
    public void modifyingOperations() {}

    @AfterReturning(pointcut = "modifyingOperations()", returning = "result")
    public void logAudit(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        String resourceName = className.replace("ServiceImpl", "");
        String actionName = methodName.toUpperCase();

        User currentUser = getCurrentUser();

        AuditLog auditLog = AuditLog.builder()
                .user(currentUser)
                .action(actionName)
                .resource(resourceName)
                .details("Action executed successfully via " + methodName)
                .build();

        auditLogRepository.save(auditLog);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            // Fetch fresh from DB to avoid detached entity issues
            return userRepository.findById(userDetails.getUser().getUserId()).orElse(null);
        }
        return null; // System-level event or unauthenticated
    }
}
