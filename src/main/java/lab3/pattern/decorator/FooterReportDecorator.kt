package lab3.pattern.decorator

import lab3.dto.PersonInfo

class FooterReportDecorator(wrapped: Report?) : ReportDecorator(wrapped) {
    override fun generate(data: MutableList<PersonInfo?>?): String {
        return super.generate(data) + "=============================\n"
    }
}